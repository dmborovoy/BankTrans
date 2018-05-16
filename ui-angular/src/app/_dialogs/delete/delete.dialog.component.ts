import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Component, Inject} from '@angular/core';
import {TransactionService} from '../../transaction/transaction.service';


@Component({
  selector: 'app-delete.dialog',
  templateUrl: '../../_dialogs/delete/delete.dialog.html',
  styleUrls: ['../../_dialogs/delete/delete.dialog.css']
})
export class DeleteDialogComponent {

  constructor(public dialogRef: MatDialogRef<DeleteDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any, public transactionService: TransactionService) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

  confirmDelete(): void {
    //this.transactionService.deleteTransaction(this.data.id);
  }
}
