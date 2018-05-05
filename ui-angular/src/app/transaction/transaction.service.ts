import {Transaction} from "./transaction.model";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Injectable} from "@angular/core";
import {log} from "util";

@Injectable()
export class TransactionService {

  constructor(private http: HttpClient) {}

  private transactionUrl = 'http://localhost:8081';

  public getTransactions(): Observable<Transaction> {
    return this.http.get<Transaction>(this.transactionUrl + '/transaction/list');
  }

  public getTransaction(id: string): Observable<Transaction> {
    return this.http.get<Transaction>(this.transactionUrl + '/transaction/' + id);
  }

}
