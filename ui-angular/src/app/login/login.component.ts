import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from '../auth/authentication.service';
import {SnackBarComponent} from "../_snackbar/snackbar.component";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  returnUrl: string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authenticationService: AuthenticationService,
              private snackBar: SnackBarComponent) {
  }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  login(username: string, password: string) {
    this.authenticationService.logout();
    this.authenticationService.login(username, password)
      .subscribe(
        data => {
          // console.log(data);
          if (this.returnUrl === '/')
            this.router.navigate(["transaction"]);
          else
            this.router.navigate([this.returnUrl]);
        },
        error => {
          if (error.status == 401) this.snackBar.openSnackBar("Wrong credentials. Please try again.");
          else {
            console.log("Error: " + error.error + " Message: " + error.message + " Status: " + error.status);
            this.snackBar.openSnackBar(error.message);
          }
          this.authenticationService.logout();
        });
  }
}
