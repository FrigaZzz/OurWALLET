import { Injectable } from '@angular/core';
import { CategoryInterface, Category } from '../types/Category';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject ,  Observable ,  SubscriptionLike as ISubscription, from, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';

const API_PATH = '/api/categories';

@Injectable({
  providedIn: 'root'
})

/**
 * @_categoriesSubject contiene tutte le categorie disponibili, anche quelle CUSTOM generate dall'utente. 
 *                     1. Prevediamo che ogni utente può generare delle categorie a suo piacere che NON SONO CONDIVISE con altri utenti? Se facciamo in questo modo, diventa difficile registrare e visualizzare
 *                      correttamente nei gruppi ogni transazione (in quanto un utente potrebbe non vedere effettivamente la categoria di una transazione poiché
 *                      quella categoria non è presente nel suo "dizionario personale di categorie"). Una soluzione potrebbe prevedere di fare un check del tipo: 
 *                      SE (transazione.Categoria) NON è RICONOSCIUTA allora si RISALE alla SUPER CATEGORIA più vicina. Abbiamo quindi una lista di CATEGORIE DEFAULT, se l'utente 
 *                      genera una CATEGORIA CUSTOM allora questa può essere sottocategoria di un'altra custom o una super. 
 *                      Questo funziona perchè il CHECK deve CONFRONTARE ID finché non si risolve la categoria SUPER.
 *                      
 *                      [NOTA 1] Nel DB bisogna però far si che CATEGORIA CUSTOM sia anche in relazione con l'utente che l'ha definita 
 *                      
 *                      [NOTA 2]: per quanto riguarda però un BUDGET questo non può essere valido all'interno di gruppi: infatti un utente che definisce un category per una 
 *                            certa CATEGORIA CUSTOM finirebbe con il risultare l'unico coinvolto nel "risparmiare" per quel genere di prodotti. Per questa ragione,
 *                            all'interno di gruppi si possono creare BUDGET solo per CATEGORIE DEFAULT oppure possiamo registrare le CATEGORIE CUSTOM sul gruppo.
 *                     
 *                      [NOTA 1 + NOTA 2 possiamo legare le CATEGORIE CUSTOM al gruppo in cui sono state definite e risolvere tutti questi conflitti. 
 *                      Avremo una piccola e plausibile incoerenza però: quando l'utente passa da un gruppo all'altro non avrà continuità nelle categorie disponibili 
 *                      per etichettare le transazioni. Credo possa essere una soluzione comunque non brutta.
 *                     2. Tutto risulta più semplice se quando un utente genera una nuova categoria, questa è disponibile per OGNI UTENTE. 
 * 
 *                     Di sequito implemento la soluzione 2 poiché più veloce e semplice. 
 */
export class CategoriesService {
  private categoryIDS=0

  private subscriptions = new Array<ISubscription>();

  private _categoriesSubject = new BehaviorSubject<Array<CategoryInterface>>(new Array<CategoryInterface>());
  public categories = this._categoriesSubject.asObservable();

  constructor(
    private http:HttpClient,
  ) {

    this.subscriptions.push(this.retrieveCategores(true).subscribe())
    
    /* solo se facciamo la distinzione
    *this.subscriptions.push(this.retrieveCustomCategores().subscribe())
    */

  }

  /**
   * Da definire la rotta per recuperare appunto le categorie. Se si seguisse 1 andrebbe benone GROUPS
   * 
   */
  retrieveCategores(init?): Observable<CategoryInterface[]>{
    if(init){
      console.log("ehehe")
     return this.http.get(API_PATH).pipe(map(
        (data:any) => {
          console.log(data)

            let  categories = data.body.categories;
            this._categoriesSubject.next(categories);
            return categories;
        },err =>console.log(err))
      )
    }
  else  return this.categories;
  }
  
  /*retrieveCustomCategores(): Observable<CategoryInterface[]>)*/

  /**
   * Accesso senza sottoscrizione
   */
  getCategories():CategoryInterface[]{
    return this._categoriesSubject.getValue()
  }

  createCategory(category): Observable<CategoryInterface> {
 
    return this.http.post(API_PATH, category).pipe(map(
        (data:any) => {
            this.retrieveCategores(true).subscribe()

            return data;
        }
    ));
  }



  updateCategory(updateCategory: CategoryInterface): Observable<CategoryInterface> {
return this.http.put(API_PATH+ `/${updateCategory.id}`, updateCategory).pipe(map(
          (data:any) => {
            this.retrieveCategores(true).subscribe()

            return data;
          }
      ));
  }

  deleteCategory(deleteCategory: CategoryInterface): Observable<CategoryInterface> {
  return this.http.delete(API_PATH + `/${deleteCategory.id}`).pipe(map(
          (data:any) => {
            this.retrieveCategores(true).subscribe()

            return data;
          }
      ));
  }
}
