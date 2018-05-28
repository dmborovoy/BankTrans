import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TransactionComponent} from './transaction.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {CustomMaterialModule} from "../material.module";
import {TransactionFilterModule} from "./filter/transaction-filter.module";
import {TransactionFilterComponent} from "./filter/transaction-filter.component";

@NgModule({
  imports: [
    NgbModule,
    CommonModule,
    CustomMaterialModule,
    TransactionFilterModule,
  ],
  declarations: [
    TransactionComponent,
  ],
  providers: [
    TransactionFilterComponent
  ]
})
export class TransactionModule {
}
