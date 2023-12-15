import { Settings } from './../../types/Settings';
import { ConfigService } from './../ConfigService/config.service';
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Injectable({ providedIn: 'root' })
export class RequestSettingsService {

    constructor(private http: HttpClient, private settingsService: ConfigService) {
    }

    initializeApp(): Promise<any> {

        return new Promise(
            (resolve) => {
                this.http.get('assets/settings.json')
                    .toPromise()
                    .then(response => {
                            this.settingsService.settings = <Settings>response;
                            resolve();
                        }
                    )
            }
        );
    }    
}