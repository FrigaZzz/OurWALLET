import { Injectable } from '@angular/core';

@Injectable()
export class JwtService {

    getAccessToken(): String {
        return localStorage.getItem("ourwallet-token")
    }

    saveToken(token_obj: any) {
          localStorage.setItem("ourwallet-token",token_obj)
    }

    destroyToken() {
      localStorage.removeItem("ourwallet-token");
    }

}
