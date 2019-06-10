import {User} from "./user.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Injectable} from "@angular/core";


@Injectable()
export class UserService {
  constructor(private http: HttpClient) {
  }

  private usersUrl = 'http://localhost:8081/auth';

  getUserDetails(): Observable<User> {
    console.log("User " + this.http.get<User>(this.usersUrl));
    return this.http.get<User>(this.usersUrl);
  }
}
