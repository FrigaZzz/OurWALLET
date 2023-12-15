# TaassAngularFrontend

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 8.3.19.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).


Transaction -- amount + account + category

Category    -- id name custom (if true -> bound to the group)

Budget      -- category + amount + perdiod + account

Goal        -- amount + goal_account 



   //aggiungere magari anche controlli che per alcuni campi non ci devono essere certe operazioni disponibili
    protected Specification<Transaction> resolveSpecificationFromFilterObject(SearchFilter searchParameters) {
        List<SpecSearchCriteria> criterias=new ArrayList<>();
        if(searchParameters==null || searchParameters.getClaims().isEmpty()){}
        else 
            //String orPredicate,  String key, String operation,  Object value)
           searchParameters.getClaims().forEach((key,search)->{
            criterias.add(new SpecSearchCriteria(search.isOrPredicate(),search.getKey(),search.getOperation(),search.getValue()));
        });
        
        TransactionSpecificationsBuilder builder = new TransactionSpecificationsBuilder(criterias);
        
        return builder.build();
    }

    @GetMapping("/transactions")
    public BaseResponse<StatusMeta,Object> getAccountTransactions(@RequestHeader(value = "Authorization") String token, String orderBy, String direction, Integer pageIndex, Integer pageSize, String filters) throws InvalidJWTTokenException, InvalidAuthorizationHeaderException, ForbiddenAccessToResourceException, ResourceNotFoundException {
        
        
        Map<String, SearchCriteria> retMap=new Gson().fromJson(
            filters, new TypeToken<HashMap<String, SearchCriteria>>() {}.getType()
        );
//        
        SearchFilter search=new SearchFilter(retMap);
        Specification<Transaction> spec = resolveSpecificationFromFilterObject(search);
//        
//        
        Direction dir=Direction.ASC;
        if(direction.toLowerCase().equals("asc"))
            dir=Direction.DESC;
        Sort sort = Sort.by(dir,orderBy);
	Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        
        Page<Transaction> out=this.transactionRepository.findAll(spec, pageable);
       
        
        long totalTransactions=out.getTotalElements();
        int totalEarnings=0;
        int totalExpanses=0;
        int earnings=0;
        int expanses=0;
        
        
        List<InputTransaction> trc=new ArrayList<>();
        out.getContent().forEach(a -> {
            InputTransaction t=new InputTransaction();
            t.setAccountSenderID(a.getAccount());
            t.setAccountReceivedID(a.getTransferTargetAccount());
            t.setDescription(a.getDescription());
            t.setAmount(a.getAmount());
            t.setDate(a.getDate());
            t.setGroupSenderID(a.getGroups());
            t.setId(a.getId());
            trc.add(t);
        });

        
        Map m=new HashMap<String,Object>();
        m.put("transactions",trc);  
        m.put("total",totalTransactions);  
        m.put("fil",(spec));  


        return new BaseResponse<>(new StatusMeta(200),m);
    }