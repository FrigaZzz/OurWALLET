import { Settings } from './../../types/Settings';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ConfigService {
    public settings: Settings;

    constructor() {
        this.settings = new Settings();
    }
}