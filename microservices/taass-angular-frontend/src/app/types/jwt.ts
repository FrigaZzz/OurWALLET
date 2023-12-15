
// static final String CLAIM_KEY_USERID = "sub";
// static final String CLAIM_KEY_USERNAME = "name";
// static final String CLAIM_KEY_EMAIL = "email";
// static final String CLAIM_KEY_CREATED = "iat";

export interface JtwInterface
{
    sub: number
    name:string
    email:string
    iat:number    
    exp:number
}
export class Jwt implements JtwInterface{
    sub: number
    name:string
    email:string
    iat:number    
    exp:number
    constructor (){
        this.sub=-1
        this.name=""
        this.email=""
        this.iat=Date.now()    
        this.exp=Date.now()
    }
}


