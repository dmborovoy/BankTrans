import {Transaction} from "./transaction.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Injectable} from "@angular/core";

@Injectable()
export class TransactionService {

  private readonly transactionUrl = 'http://localhost:8081/transaction/';

  constructor(private http: HttpClient) {
  }


  getTransactions(): Observable<Transaction> {
    return this.http.get<Transaction>(this.transactionUrl + 'list' + '?sort=id');
  }

  getTransaction(id: string): Observable<Transaction> {
    return this.http.get<Transaction>(this.transactionUrl + id);
  }

  updateTransaction(id: string, transaction: Transaction): Observable<Transaction> {
    console.log("Update service call");
    console.log(id, transaction);
    return this.http.put<Transaction>(this.transactionUrl + id, transaction);
  }
}
