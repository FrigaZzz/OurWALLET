import { DataSource } from '@angular/cdk/table';
import { Transaction, TransactionInterface } from './Transaction';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { TransactionService } from '../services/transaction.service';
import { CollectionViewer } from '@angular/cdk/collections';
import { catchError, finalize } from 'rxjs/operators';
import { TransactionResult } from './transaction-result';

/**
 * @loading              usata per mostrare uno spinner per il loading
 * @loadingSubject
 * @_transactionsSubject conterrà il vettore di transazioni più recente
 */
export class TransactionDataSource implements DataSource<TransactionInterface> {

    private _transactionsSubject = new BehaviorSubject<TransactionInterface[]>([]);
    private _loadingSubject = new BehaviorSubject<boolean>(false);
    private _totalResults = new BehaviorSubject<number>(0);
    private _earnings = new BehaviorSubject<number>(0);
    private _expanses = new BehaviorSubject<number>(0);
    private _totalEarnings = new BehaviorSubject<number>(0);
    private _totalExpanses = new BehaviorSubject<number>(0);
    private _balance = new BehaviorSubject<number>(0);

    public loading$ = this._loadingSubject.asObservable();
    public total$ = this._totalResults.asObservable();
    public earnings$ = this._earnings.asObservable();
    public expanses$ = this._expanses.asObservable();
    public totalEarnings$ = this._totalEarnings.asObservable();
    public totalExpanses$ = this._totalExpanses.asObservable();
    public balance$ = this._balance.asObservable();

    constructor(private transactionService: TransactionService) {}

    /**
     * non espongo il reference al subject per evitare che questo sia alterato
     */
    connect(collectionViewer: CollectionViewer): Observable<TransactionInterface[]> {
        return this._transactionsSubject.asObservable();
    }
      /**
     * non espongo il reference al subject per evitare che questo sia alterato
     */
    data(): TransactionInterface[] {
        let data=[]
        if(this._transactionsSubject.value)
            this._transactionsSubject.value.forEach(d=>data.push(d))
        return data;
    }

    disconnect(collectionViewer: CollectionViewer): void {
        this._transactionsSubject.complete();
        this._loadingSubject.complete();
        this._totalResults.complete();
        this._earnings.complete();
        this._expanses.complete();
        this._totalEarnings.complete();
        this._totalExpanses.complete();
        this._balance.complete();

    }

    /**
     * Il metodo è richiamato per ogni interazione dell utente con la table
     * La sottoscrizione all'output del metodo causa la richiesta http
     * La presenza di Group ID e AccountID è mutuamente esclusiva 
     *
     * @param filter 
     * @param active 
     * @param direction 
     * @param pageIndex 
     * @param pageSize 
     * @param groupID        
     * @param accountID 
     */
    loadTransactions(filter:string="",active: string="date", direction: string="desc", pageIndex: number=0, pageSize: number=10,groupID=undefined,accountID=undefined) {

        this._loadingSubject.next(true);

        this.transactionService.retrieveTransactions(filter, active,direction,pageIndex, pageSize,groupID,accountID).pipe(
            catchError(() => of([])),
            finalize(() => this._loadingSubject.next(false))
        )
        .subscribe((payload:TransactionResult) => {
            this._loadingSubject.next(false),
            this._earnings.next(payload.earnings)
            this._expanses.next(payload.expanses)
            this._totalEarnings.next(payload.totalEarnings)
            this._totalExpanses.next(payload.totalExpanses)
            this._totalResults.next(payload.resultLenght)
            this._transactionsSubject.next(payload.transactions)
            this._balance.next(payload.balance)

        });
    }    
}