import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import * as auth0 from 'auth0-js';

@Injectable()
export class AuthService {

  auth0 = new auth0.WebAuth({
    clientID: 'ilccw9WLY5UE2JZAPAn2BbQmlHnzLoMK',
    domain: 'alokpatel.auth0.com',
    responseType: 'token id_token',
    audience: 'https://alokpatel.auth0.com/userinfo',
    redirectUri: 'http://localhost:4200/product/111',
    scope: 'openid'
  });

  constructor(public router: Router) { }

  public login(): void {
    this.auth0.authorize();
  }

}
