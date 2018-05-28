import { Component, OnInit } from '@angular/core';
import {TransactionService} from '../transaction/transaction.service';
import {TransactionComponent} from "../transaction/transaction.component";
import {Transaction} from "../transaction/transaction.model";
import {MatTableDataSource} from "@angular/material";
import {log} from "util";
import {Router} from "@angular/router";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router, private transactionService: TransactionService) { }

  dataSource = new MatTableDataSource<Transaction>();


  ngOnInit() {
  }

  find(id) : void {
    this.router.navigate(["transaction", id]);
  }

  listAll() : void {
    this.router.navigate(["transaction"]);
  }

  login() : void {
    this.router.navigate(["login"]);
  }



}
