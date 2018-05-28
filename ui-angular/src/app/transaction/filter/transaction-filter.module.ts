import {NgModule} from "@angular/core";
import {TransactionFilterComponent} from "./transaction-filter.component";
import {CustomMaterialModule} from "../../material.module";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    TransactionFilterComponent
  ],
  imports: [
    CustomMaterialModule,
    FormsModule
  ],
  exports: [
    TransactionFilterComponent
  ]
})

export class TransactionFilterModule {}
