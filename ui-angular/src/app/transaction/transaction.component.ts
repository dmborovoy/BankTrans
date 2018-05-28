import {AfterViewInit, Component, NgModule, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatPaginator, MatSelect, MatSort, MatTableDataSource} from '@angular/material';
import {Transaction} from './transaction.model';
import {TransactionService} from './transaction.service';
import {ActivatedRoute, Router} from '@angular/router';
import {SnackBarComponent} from "../_snackbar/snackbar.component";
import {AuthenticationService} from "../auth/authentication.service";
import {catchError} from "rxjs/operators";
import {map} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {startWith} from "rxjs/operators";
import {merge} from "rxjs/observable/merge";
import { of as observableOf } from 'rxjs/observable/of';
import {TransactionDetailsComponent} from "./details/transaction-details.component";
import {TransactionFilterComponent} from "./filter/transaction-filter.component";
import {flatMap} from "tslint/lib/utils";

// @NgModule({
//   imports: [
//     TransactionFilter
//   ],
// })

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit, AfterViewInit {

  // exDisplayedColumns = ['id', 'date', 'entryDate', 'fundsCode', 'amount', 'currency',
  //   'swiftCode', 'referenceForAccountOwner', 'referenceForBank', 'transactionDescription', 'statementID',
  //   'transactionStatus', 'errorDescription', 'instance', 'entryOrder', 'informationToAccountOwner'];
  displayedColumns = ['id', 'statementId', 'amount', 'currency', 'date', 'transactionDescription', 'referenceForAccountOwner',
    'instance', 'fundsCode', 'status', 'actions'];
  dataSource: MatTableDataSource<Transaction> = new MatTableDataSource<Transaction>();

  resultsLength = 0;
  isLoadingResults = false;
  isRateLimitReached = false;

  index: number;
  id: number;
  instance: string;
  editable = false;


  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(TransactionFilterComponent) transactionFilter;

  constructor(private dialog: MatDialog,
              private route: ActivatedRoute,
              private router: Router,
              private transactionService: TransactionService,
              private snackBar: SnackBarComponent,
              private authService: AuthenticationService,
              ) {
  }


  ngOnInit(): void {
    //TODO Uncomment (Or remove)
    // this.route.params.subscribe(params => {
    //   if (params['id']) {
    //     this.getTransaction(params['id'])
    //   }
    //   else this.getTransactions();
    // });
    this.editable = this.authService.isAdmin();
    console.log("User is admin = " + this.authService.isAdmin());
    //this.dataSource.sort = this.sort;
  }

  public ngAfterViewInit() {
    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => {this.paginator.pageIndex = 0});
    this.transactionFilter.fieldChange.subscribe(() => {this.paginator.pageIndex = 0});

    merge(this.sort.sortChange, this.paginator.page,
      this.transactionFilter.fieldChange
    )
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.transactionService.fetchLatest(this.sort.active, this.sort.direction,
            this.paginator.pageIndex, this.paginator.pageSize, this.transactionFilter.filterParams)
            //(totalElements) =>  this.resultsLength = totalElements);
        }),
    map(data => {
          this.isLoadingResults = false;
          this.isRateLimitReached = false;
          //alternatively to response headers;
          this.resultsLength = data.totalElements;
          data.content.forEach((t) => {
            t.date = (new Date(t.date)).toLocaleDateString()
          });
          return data;
        }),
        // map(data => {
        //
        //   return data;
        // }),
        catchError(() => {
          this.isLoadingResults = false;
          this.isRateLimitReached = true;
          return observableOf([]);
        })
      ).subscribe((data:any) => {
        console.log("changes");
      console.log(data);
      this.dataSource.data = <Transaction[]>data.content;
    });
  }

  // getTransactions(): void {
  //   this.transactionService.getTransactions().subscribe(
  //     data => {
  //       console.log(data);
  //       this.dataSource.data = data.content;
  //     }
  //   );
  // }

  // getTransaction(id: string): void {
  //   this.transactionService.getTransaction(id).subscribe(
  //     data => {
  //       this.dataSource.data = Array(data);
  //     }
  //   );
  // }

  updateInstance(transactionId: string, instance: string) {
    console.log(transactionId, instance);
    this.transactionService.updateTransaction(transactionId, <Transaction> {instance: instance}).subscribe(
      data => {
        this.snackBar.openSnackBar("Instance updated");
      },
      error => {
        this.snackBar.openSnackBar(error.message);
        console.log("Error: " + error.error + " Message: " + error.message + " Status: " + error.status);
      }
    );
  }

  updateStatus(transactionId: string, status: string) {
    console.log(transactionId, status);
    this.transactionService.updateTransaction(transactionId, <Transaction> {status: status}).subscribe(
      data => {
        this.snackBar.openSnackBar("Status updated");
      },
      error => {
        this.snackBar.openSnackBar(error.message);
        console.log("Error: " + error.error + " Message: " + error.message + " Status: " + error.status);
      }
    );
  }

  showDetails(id: number) {
    console.log(id);
    const dialogRef = this.dialog.open(TransactionDetailsComponent, {data : {id:id} });
  }

  // cancelOpen(select: MatSelect) {
  //   select.panelClass = 'hidden';
  //   select.close();
  //   select.setDisabledState(true);
  //   //select.panelClass === 'hidden' ? select.close() : null;
  // }
}

