
export interface CategoryInterface
{
    id: number
    name: string
    superCategory?: CategoryInterface
}
export class Category implements CategoryInterface{
    id: number
    name: string
    superCategory?: CategoryInterface

    constructor (jsonObjetct={id:-1,superCategory:null,name:""}){
        this.id=jsonObjetct.id
        this.superCategory=jsonObjetct.superCategory    
        this.name=jsonObjetct.name
    }
}


