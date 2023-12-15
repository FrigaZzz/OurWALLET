package com.mfr.taass.spring.stats.api.controllers;

import com.mfr.taass.spring.stats.api.beans.BarChartData;
import com.mfr.taass.spring.stats.api.beans.BaseResponse;
import com.mfr.taass.spring.stats.api.beans.BudgetGraph;
import com.mfr.taass.spring.stats.api.beans.GoalData;
import com.mfr.taass.spring.stats.api.beans.OutputBudget;
import com.mfr.taass.spring.stats.api.beans.OutputGoal;
import com.mfr.taass.spring.stats.api.beans.PieChartData;
import com.mfr.taass.spring.stats.api.beans.StatusMeta;
import com.mfr.taass.spring.stats.api.checks.Correctness;
import com.mfr.taass.spring.stats.api.entities.Budget;
import com.mfr.taass.spring.stats.api.entities.Goal;
import com.mfr.taass.spring.stats.api.entities.Groups;
import com.mfr.taass.spring.stats.api.entities.Transaction;
import com.mfr.taass.spring.stats.api.entities.User;
import com.mfr.taass.spring.stats.api.exceptions.InvalidAuthorizationHeaderException;
import com.mfr.taass.spring.stats.api.exceptions.InvalidJWTTokenException;
import com.mfr.taass.spring.stats.api.repos.AccountRepository;
import com.mfr.taass.spring.stats.api.repos.CategoryRepository;
import com.mfr.taass.spring.stats.api.repos.GroupsRepository;
import com.mfr.taass.spring.stats.api.repos.TransactionRepository;
import com.mfr.taass.spring.stats.api.repos.UserRepository;
import com.mfr.taass.spring.stats.api.utils.JwtUser;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    @Autowired
    private Correctness correctness;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupsRepository groupRespository;
    @Autowired
    private CategoryRepository categoryRespository;

    // NOTA: l'url base in questo caso è impostato a "/api/stats" invece di "/api"
    // NOTA: conta solo entrate e uscite, non trasferimenti
    @ApiOperation(value = "Get all expenses and the incomes of a determined year.", notes = "You will get the total of the Expenses and Incomes, suddivided by month.")
    @GetMapping("/IncomeExpenseData/{year}")
    public BaseResponse<StatusMeta, Object> getIncomeExpanses(@RequestHeader(value = "Authorization") String token, @PathVariable("year") int year) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException {
        JwtUser user = checkToken(token);

        BarChartData data = new BarChartData();
        data.addLabel("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dic");

        data.addNewSeries("Entrate");
        data.addNewSeries("Uscite");
        data.build();

        // fake data
        // data.addDataToSeries("Entrate", 2065, 2509, 1800, 1181, 2056, 2755, 1365, 1659, 1880, 2281, 1256, 1555);
        // data.addDataToSeries("Uscite", 1965, 2009, 1900, 1081, 1046, 3057, 1065, 1259, 1580, 2080, 1856, 1655);
        User u = userRepository.findById(user.getId()).get();
        Groups family = u.getFamilyGroup();
        List<Transaction> transactions = transactionRepository.findByGroupsAndDateBetween(family, getStartTimeOfYear(year), getEndTimeOfYear(year));
        transactions = transactions.stream().filter(t -> t.getTransferTargetAccount() == null).collect(Collectors.toList());

        if (transactions.size() > 0) {
            transactions.sort(Comparator.comparing(Transaction::getDate));
            int lastMonth = getMonthFromTimestamp(transactions.get(0).getDate());
            int month = lastMonth;

            int income = 0; // TODO usare long, anche in SeriesData
            int expense = 0;

            for (Transaction t : transactions) {
                month = getMonthFromTimestamp(t.getDate());

                long amount = t.getAmount();

                if (month != lastMonth) { // inserisco in data
                    while (data.getDataset().get(0).getData().size() < lastMonth - 1) {
                        data.addDataToSeries("Entrate", 0);
                        data.addDataToSeries("Uscite", 0);
                    }
                    data.addDataToSeries("Entrate", income);
                    data.addDataToSeries("Uscite", expense);
                    income = 0;
                    expense = 0;
                    lastMonth = month;
                }

                if (amount > 0) {
                    income += amount;
                } else {
                    expense += (-amount);
                }
            }

            // inserisco ultimo mese
            while (data.getDataset().get(0).getData().size() < month) {
                data.addDataToSeries("Entrate", 0);
                data.addDataToSeries("Uscite", 0);
            }

            data.addDataToSeries("Entrate", income);
            data.addDataToSeries("Uscite", expense);

            while (data.getDataset().get(0).getData().size() < 12) {
                data.addDataToSeries("Entrate", 0);
                data.addDataToSeries("Uscite", 0);
            }
        } else {
            for (int i = 0; i < 12; i++) {
                data.addDataToSeries("Entrate", 0);
                data.addDataToSeries("Uscite", 0);
            }
        }

        data.build();
        return new BaseResponse<>(new StatusMeta(200), data);
    }

    @ApiOperation(value = "Get the progress of the Budget of your familyGroup.", notes = "You will get a List of <BudgetGraph>, containing the amount of money you can spend today (for the category relative to the Budget), how much you have spent for the Budget and the description of it.")
    @GetMapping("/familyBudgetSuggestions")
    public BaseResponse<StatusMeta, Object> familyBudgetSuggestions(@RequestHeader(value = "Authorization") String token) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException {
        JwtUser user = checkToken(token);
        List<BudgetGraph> dailyBudgets = new ArrayList<>();

        User u = userRepository.findById(user.getId()).get();
        Groups family = u.getFamilyGroup();
        List<Budget> budgets = family.getBudgets();
        budgets = budgets.stream().filter(b -> b.getCategory().getSuperCategory() == null).collect(Collectors.toList());

//        Calendar today = Calendar.getInstance();
//        Calendar yesterday = (Calendar) today.clone();
//        yesterday.add(Calendar.DAY_OF_MONTH, -1);
//        yesterday.set(Calendar.HOUR, 11);
//        yesterday.set(Calendar.MINUTE, 59);
//        yesterday.set(Calendar.SECOND, 59);
//        yesterday.set(Calendar.AM_PM, Calendar.PM);


        Date date = new Date();
        int y = date.getYear();
        int m = date.getMonth();
        
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day=cal.get(Calendar.DAY_OF_MONTH);
       

        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int remainingDays = daysInMonth - cal.get(Calendar.DAY_OF_MONTH);

        
        Date firstDay = new Date(y, m , 1);
        Date lastDay = new Date(y, m, daysInMonth);
        
        
        
        // Prendere tutte le transazioni in uscita della famiglia relative a una categoria
        for (Budget b : budgets) {
            List<Transaction> transactions = transactionRepository.findByGroupsAndCategoryAndTransferTargetAccountAndAmountLessThanAndDateBetween(family, b.getCategory(), null, 0L,firstDay.getTime(),lastDay.getTime() + 3600 * 1000 * 24 - 1);//getStartTimeOfMonth(today.get(Calendar.YEAR), today.get(Calendar.MONTH)), yesterday.getTimeInMillis());
            Long sum = 0L;
            for (Transaction t : transactions) {
                if (t.getAmount() < 0) {
                    sum += (-t.getAmount());
                }
            }
            Long bud = b.getBudgetAmount() - (b.getBudgetAmount() / daysInMonth) * remainingDays - sum;
            bud = Math.max(0, bud);
            dailyBudgets.add(new BudgetGraph(bud, sum, new OutputBudget(b)));
        }

        return new BaseResponse<>(new StatusMeta(200), dailyBudgets);
    }

    @GetMapping("/goalSuggestions/{groupID}")
    public BaseResponse<StatusMeta, Object> getGoalsStats(@RequestHeader(value = "Authorization") String token, @PathVariable("groupID") Long groupID) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException {
        JwtUser user = checkToken(token);
        List<GoalData> dailyGoals = new ArrayList<>();

        User u = userRepository.findById(user.getId()).get();
        Groups target = (u.getFamilyGroup().getId().equals(groupID)) ? u.getFamilyGroup() : null;

        if (target == null) {
            Optional<Groups> target2 = (u.getCommonFundGroups().stream().anyMatch(g -> g.getId() == groupID))
                    ? u.getCommonFundGroups().stream().filter(g -> g.getId() == groupID).collect(Collectors.reducing((a, b) -> null))
                    : null;
            if (target2 != null) {
                target = target2.get();
            } else {
                throw new InvalidAuthorizationHeaderException();
            }
        }

        List<Goal> goals = target.getGoals();

        // Prendere tutte le transazioni in uscita della famiglia relative a una categoria
        for (Goal g : goals) {

            Date d = new Date();

            long totalDays = (g.getStartDate() - g.getDeadLine()) / (24 * 60 * 60 * 1000);
            long remainingDays = (g.getDeadLine() - d.getTime()) / (24 * 60 * 60 * 1000);

            OutputGoal out = OutputGoal.fromGoal(g, groupID);
            long sum = out.getAmountReached();
            Long todaySave = (g.getAmount() - sum) / remainingDays;
            dailyGoals.add(new GoalData(todaySave, sum, out));
        }

        return new BaseResponse<>(new StatusMeta(200), dailyGoals);

    }
    @ApiOperation(value = "Get pie chart of categories expanses of a determined group.", notes = "If you have the permissions, you will get a List of <PieChartData>, containing the expanses over each categories.")
    @GetMapping("/getPieChartData/{groupID}")
    public BaseResponse<StatusMeta, Object> getPieChartData(@RequestHeader(value = "Authorization") String token, @PathVariable("groupID") Long groupID) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException {
        JwtUser user = checkToken(token);

        PieChartData pieChart = new PieChartData();

        User u = userRepository.findById(user.getId()).get();
        Groups target = (u.getFamilyGroup().getId().equals(groupID)) ? u.getFamilyGroup() : null;

        if (target == null) {
            Optional<Groups> target2 = (u.getCommonFundGroups().stream().anyMatch(g -> g.getId() == groupID))
                    ? u.getCommonFundGroups().stream().filter(g -> g.getId() == groupID).collect(Collectors.reducing((a, b) -> null))
                    : null;
            if (target2 != null) {
                target = target2.get();
            } else {
                throw new InvalidAuthorizationHeaderException();
            }
        }

        Date date = new Date();
        int y = date.getYear();
        int m = date.getMonth();
        
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day=cal.get(Calendar.DAY_OF_MONTH);
       

        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int remainingDays = daysInMonth - cal.get(Calendar.DAY_OF_MONTH);

        
        Date firstDay = new Date(y, m , 1);
        Date lastDay = new Date(y, m, daysInMonth);
        

        List<Transaction> transactions = this.transactionRepository.findByGroupsAndDateBetween(target, firstDay.getTime(), lastDay.getTime() + 3600 * 1000 * 24 - 1);
        HashMap<String, Long> map = new HashMap<>();

        transactions.forEach(t -> {
            if (t.getAmount() < 0 && t.getTransferTargetAccount() == null) {
                if (!map.containsKey(t.getCategory().getName())) {
                    map.put(t.getCategory().getName(), 0L);
                }
                map.replace(t.getCategory().getName(), map.get(t.getCategory().getName()) + Math.abs(t.getAmount()));
            }
        });

        map.forEach((key, value) -> {
            pieChart.addData(key, value);
        });

        return new BaseResponse<>(
                new StatusMeta(200), pieChart);
    }

    private JwtUser checkToken(String token) throws InvalidAuthorizationHeaderException, InvalidJWTTokenException {
        if (!token.startsWith("Bearer ")) {
            throw new InvalidAuthorizationHeaderException();
        }
        token = token.substring(7, token.length());
        JwtUser user = correctness.checkJWT(token);
        return user;
    }

    private long getStartTimeOfYear(int year) {
        return getStartTimeOfMonth(year, Calendar.JANUARY);
    }

    private long getEndTimeOfYear(int year) {
        return getEndTimeOfMonth(year, Calendar.DECEMBER);
    }

    private long getStartTimeOfMonth(int year, int month) {
        Calendar mycalstart = new GregorianCalendar(year, month, 1);
        return mycalstart.getTimeInMillis();
    }

    private long getEndTimeOfMonth(int year, int month) {
        Calendar mycal = new GregorianCalendar(year, month, 1);
        int lastDate = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        mycal = new GregorianCalendar(year, month, lastDate);
        long endDate = mycal.getTimeInMillis();
        endDate += 24 * 3600 * 1000 - 1;
        return endDate;
    }

    private int getMonthFromTimestamp(long timestamp) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.MONTH);
    }
}
