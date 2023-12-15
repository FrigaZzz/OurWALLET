//fake DB


/**
 * PRELIEVO/DEPOSITO  >     transfer target account è null
 *                    >     importa il segno
 *                    >     si considera il gruppo famiglia
 * 
 * SPESA BUDGET       >      transfer target account è conto bersaglio
 *                    >      group è il gruppo che effettua il pagamento**
 * *un gruppo effettua automaticamente versamenti verso i budget o goals aperti e/o è permesso agli admin del gruppo  
 */

 
transactions:[

    // A aggiunge 100
    {
        id: 0,
        amount: 100,
        account: 0,
        group:   0,
        //transfer_target_account: 2, //se manca allora è un inserimento nel proprio conto
        category: 0//"Deposit"
    },
    //A usa 30 in CIBO
    {
        id: 1,
        amount: -30,
        account: 0,
        group:   0,
        //transfer_target_account: 1, //se manca allora è un inserimento nel proprio conto
        category: 2//"Food"
    },
    //A mette a disposizione 10  per il gruppo cassa comune target
    {
        id: 2,
        amount: 10,
        account: 0,
        group:   0,
        transfer_target_account: 2,
        category: 1//"Transfer"
    },
]

users:[
    {
        id:0 ,
        email: "admin@email.it",
        username: "admin",
        password: "admin",
        //payer:"boolean",
        familyGroup: 0,
        accounts: [0]
        //commonFundGroups:Array<GroupInterface>
    },
    {
        id:1 ,
        email: "user@email.it",
        username: "user",
        password: "user",
        //payer:"boolean",
        familyGroup: 1,
        accounts: [1]
        //commonFundGroups:Array<GroupInterface>
    },

]

groups:[
    {
        id: 0,
        amount: 0,
        isFamilyGroup:true,
        //transactions:Array<TransactionInterface>,
        //goals:Array<GoalInterface>,
        //budget:Array<BudgetInterface>,
        //members:Array<UserInterface>,
        account: 0,
        name: "Utente Admin",
        description:"Gruppo default",
    },
    {
        id: 1,
        amount: 0,
        isFamilyGroup:true,
        //transactions:Array<TransactionInterface>,
        //goals:Array<GoalInterface>,
        //budget:Array<BudgetInterface>,
        //members:Array<UserInterface>,
        account: 1,
        name: "Utente User",
        description:"Gruppo default",
    },
    {
        id: 2,
        amount: 0,
        isFamilyGroup:false,
        //transactions:Array<TransactionInterface>,
        //goals:Array<GoalInterface>,
        //budget:Array<BudgetInterface>,
        //members:Array<UserInterface>,
        account: 2,
        name: "Gruppo acquisto 1",
        description:"Regalo compleanno",
    },
]

goals:[
    {
        id: 0,
        name: "bici",
        //deadline:Date
        amount:150,
        account: 3  ,  
        group:0,
        description:"acquisto bici"
    },
    {
        id: 1,
        name: "pc",
        //deadline:Date
        amount:450,
        account: 4 ,  
        group:0,
        description:"acquisto bici"
    },
    
]

budgets:[
    {
        id: 0,
        //deadline:Date
        budgetAmount:250,
        group:0,
        category: 2//"Food"

    },
    {
        id: 1,
        //deadline:Date
        budgetAmount:100,
        group:0,
        category: 3//"Gas"
    }
]

category:[
    {
        id:0,
        name:"Deposit"
    },
    {
        id:1,
        name:"Transfer"
    },
    {
        id:2,
        name:"Food"
    },
    {
        id:3,
        name: "Gas"
    }
]

accounts:[
    {
        name: "Admin default",
        id: 0,
        //goals:Array<GoalInterface>,
        owner: 0
        //transactions:Array<TransactionInterface>,  
    },
    {
        name: "User default",
        id: 1,
        //goals:Array<GoalInterface>,
        owner:1 
        //transactions:Array<TransactionInterface>,  
    }, 
    {
        name: " VISA Admin ",
        id: 2,
        //goals:Array<GoalInterface>,
        owner: 0
        //transactions:Array<TransactionInterface>,  
    },
    {
        name: " GOAL  BICI",
        id: 3,
        //goals:Array<GoalInterface>,
        owner: 0
        //transactions:Array<TransactionInterface>,  
    }, 
    {
        name: " GOAL PC ",
        id: 4,
        //goals:Array<GoalInterface>,
        owner: 0
        //transactions:Array<TransactionInterface>,  
    },  
]

/**
 * STATO DA RAPPRESENTARE:
 *  - A ha un conto 
 *  - A ha ricaricato di 100
 *  - A ha speso 30 in Food
 *  - A appartiene ad un gruppo acquisto
 *  - A ha inviato 10 al gruppo acquisto
 *  - A ha 2 goal: acquisatre un PC ed una bici, per i quali non sono ancora stati aggiunti fondi
 *  - A ha 2 budget da raggiungere entro una certa data
 */ 


/**
 * USER JWT
 * email
 * username
 * 
 */


/** TRANSACTIONS PAGE 
 *   -nella request q= [from,transactionID]
 *   -massimo 100 risultati ad Angular (riduzione chiamate al server)
 *  - 
 */
transactionTable:{
    meta : {
        from:0
        pagination: 10
    }
    accounts:[
        {
            account:{
                name: "Admin default",
                id: 0,
                owner:{
                    id:0 ,
                    email: "admin@email.it",
                    username: "admin",
                },
            },
            transactions:[
                {
                    id: 0,
                    amount: 100,
                    group:   0,
                    category:  {
                        id:0,
                        name:"Deposit"
                    },
                },
                {
                    id: 1,
                    amount: -30,
                    group:   0,
                    category:   {
                        id:2,
                        name:"Food"
                    },
                },
                {
                    id: 2,
                    amount: 10,
                    group:   0,
                    transfer_target_account: 2,
                    category:  {
                        id:1,
                        name:"Transfer"
                    },
                }
            ]
        }
    ]
}


/** DASHBOARD PAGE farla alla fine 
 *  - mostrare statistiche -> contattare stats api
 *  - 
 */
dashBoardData:[

]