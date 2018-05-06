import {User} from "./user.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Injectable} from "@angular/core";


@Injectable()
export class UserService {
  constructor(private http: HttpClient) {
  }

  private usersUrl = 'http://localhost:8081/users';

  getUserDetails(): Observable<User> {
    return this.http.get<User>(this.usersUrl);
  }
}
