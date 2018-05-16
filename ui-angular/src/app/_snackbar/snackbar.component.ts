import {Component, OnInit} from '@angular/core';
import {MatSnackBar} from '@angular/material';


@Component({
  selector: 'snackbar',
  templateUrl: 'snackbar.component.html',
  // styleUrls: ['snackbar.component.css'],
})
export class SnackBarComponent implements OnInit {
  ngOnInit() {
  }

  constructor(public snackBar: MatSnackBar) {
  }

  openSnackBar(message, action?) {
    this.snackBar.open(message, action, {
      duration: 2500,
    });
  }
}
