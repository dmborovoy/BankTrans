import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import {CustomMaterialModule} from "./material.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {InterceptorService} from "./auth/interceptor.service";
import {AuthenticationService} from "./auth/authentication.service";
import {LoginService} from "./login/login.service";
import {TransactionService} from "./transaction/transaction.service";
import { HomeComponent } from './home/home.component';
import {AlertService} from "./_services/alert.service";
import {UserService} from "./user/user.service";
import {AuthGuard} from "./_guards/auth.guard";
import {TransactionModule} from "./transaction/transaction.module";
import {SnackBarComponent} from "./_snackbar/snackbar.component";
import {TransactionDetailsComponent} from "./transaction/details/transaction-details.component";


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    SnackBarComponent,
    TransactionDetailsComponent
  ],
  imports: [
    NgbModule.forRoot(),
    CustomMaterialModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    TransactionModule,
  ],

  // exports: [
  // ],

  entryComponents: [
    TransactionDetailsComponent
  ],

  providers: [AuthenticationService,
    LoginService,
    TransactionService,
    AlertService,
    UserService,
    AuthGuard,
    SnackBarComponent,
    {provide: HTTP_INTERCEPTORS,
    useClass: InterceptorService,
    multi : true}
],
  bootstrap: [AppComponent]
})
export class AppModule { }
