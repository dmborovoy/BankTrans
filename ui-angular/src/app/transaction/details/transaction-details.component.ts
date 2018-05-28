import {MAT_DIALOG_DATA, MatDialogRef, MatTableDataSource} from '@angular/material';
import {Component, Inject, OnInit} from '@angular/core';
import {TransactionService} from "../transaction.service";
import {TransactionDetails} from "./transaction-details.model";

@Component({
  selector: 'transaction-details',
  templateUrl: '../../transaction/details/transaction-details.component.html',
  styleUrls: ['../../transaction/details/transaction-details.component.css']
})

export class TransactionDetailsComponent implements OnInit {

  dataSource: MatTableDataSource<TransactionDetails> = new MatTableDataSource<TransactionDetails>();
  displayedColumns = ['name', 'value'];

  constructor(private dialogRef: MatDialogRef<TransactionDetailsComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any,
              private transactionService: TransactionService) {
  }

  ngOnInit(): void {
    this.transactionService.getTransaction(this.data.id).subscribe(
      data => {
        for (let entry of Object.entries(data)) {
          //console.log(entry);
          this.dataSource.data.push({name: entry[0], value: entry[1]});
          this.dataSource.filter = '';
        }
        //console.log(this.detailsDataSource.data)
      });
   }


  onCloseClick(): void {
    this.dialogRef.close();
  }
}
