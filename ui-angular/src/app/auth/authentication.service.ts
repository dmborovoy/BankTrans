import { Injectable } from '@angular/core';
import {log} from "util";
import {UserService} from "../user/user.service";
import 'rxjs/add/operator/map'
import {Authority} from "../user/user.model";


const TOKEN_KEY = 'AuthToken';
const AUTHORITIES_KEY = 'Authorities';
@Injectable()
export class AuthenticationService {

  constructor(
    private userService: UserService) { console.log(userService)}

  setToken(token: string) {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public setAuthorities(authoritiesArray: Authority[]) {
    console.log(authoritiesArray);
    let authorities:string = '';
    for (let authority of authoritiesArray) {
      authorities += authority.authority;
    }
    log("Saving authorities: " + authorities);
    window.sessionStorage.removeItem(AUTHORITIES_KEY);
    window.sessionStorage.setItem(AUTHORITIES_KEY,  authorities);
  }

  public getAuthorities() {
    log("Getting authorities: " + sessionStorage.getItem(AUTHORITIES_KEY));
    return sessionStorage.getItem(AUTHORITIES_KEY);
  }


  login(username: string, password: string) {
    this.setToken(btoa(username + `:` + password));
    return this.userService.getUserDetails().map(
      user => {
        if (user && user.authorities) {
          console.log(user);
          this.setAuthorities(user.authorities);
        }
        return user;
      });
  }

  logout() {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.clear();
  }

  isAdmin() {
    if(sessionStorage.getItem(AUTHORITIES_KEY)===null) return false;
    return (sessionStorage.getItem(AUTHORITIES_KEY).indexOf("ROLE_UI_ADMIN") != -1);
  }
}
