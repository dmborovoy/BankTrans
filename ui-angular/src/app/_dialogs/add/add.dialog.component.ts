import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Component, Inject} from '@angular/core';
import {TransactionService} from '../../transaction/transaction.service';
import {FormControl, Validators} from '@angular/forms';
import {Transaction} from '../../transaction/transaction.model';

@Component({
  selector: 'app-add.dialog',
  templateUrl: '../../_dialogs/add/add.dialog.html',
  styleUrls: ['../../_dialogs/add/add.dialog.css']
})

export class AddDialogComponent {
  constructor(public dialogRef: MatDialogRef<AddDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: Transaction,
              public dataService: TransactionService) { }

  formControl = new FormControl('', [
    Validators.required
    // Validators.email,
  ]);

  getErrorMessage() {
    return this.formControl.hasError('required') ? 'Required field' :
      this.formControl.hasError('email') ? 'Not a valid email' :
        '';
  }

  submit() {
    // emppty stuff
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  public confirmAdd(): void {
    //this.dataService.addTransaction(this.data);
  }
}
