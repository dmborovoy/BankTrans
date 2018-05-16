import {NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MatButtonModule, MatCardModule, MatDialogModule, MatIconModule, MatInputModule, MatProgressSpinnerModule,
  MatTableModule,
  MatToolbarModule,
  MatSelectModule,
  MatSnackBarModule, MatSortModule
} from '@angular/material';

@NgModule({
  imports: [CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatDialogModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatSelectModule,
    MatSnackBarModule,
    MatSortModule],
  exports: [CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatDialogModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatSelectModule,
    MatSnackBarModule,
    MatSortModule],
})
export class CustomMaterialModule { }
