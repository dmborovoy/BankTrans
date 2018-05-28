import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {TransactionComponent} from "./transaction/transaction.component";
import {HomeComponent} from "./home/home.component";

const routes: Routes = [
  {path: 'login', component: LoginComponent, data: { title: "Login" }},
  {path: '', component: LoginComponent, data: { title: "Login" }}, /*, canActivate: [AuthGuard]*/
  {path: 'transaction', component: TransactionComponent, data: { title: "Transactions" }},
  {path: 'transaction/:id', component: TransactionComponent, data: { title: "Transaction" }}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
