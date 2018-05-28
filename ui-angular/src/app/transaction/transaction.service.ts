import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {Inject, Injectable} from "@angular/core";
import {AuthenticationService} from "../auth/authentication.service";
import {stringify} from "querystring";
import {Transaction} from "./transaction.model";
import {TransactionDetails} from "./details/transaction-details.model";
import {TransactionFilterComponent} from "./filter/transaction-filter.component";

@Injectable()
export class TransactionService {

  private apiPrefix = 'http://localhost:8081/transaction/';
  private apiEndpoint = this.apiPrefix + 'list';

  constructor( @Inject(HttpClient) private http: HttpClient,
               @Inject(AuthenticationService) private authService: AuthenticationService) {

  }


  public fetchLatest(sort: string = '', order: string = '', page: number = 1, perPage: number = 5, filter?: TransactionFilterComponent): Observable<Transaction> {
    return this.http.get<Transaction>(this.apiEndpoint +'?' + TransactionService.createUrlQuery({
      sort: {field: sort, order: order},
      pagination: {page, perPage},
      filter
    }));
  }

  //should be put in a util
  static createUrlQuery(params: any) {
    if (!params) {
      return "";
    }


    let page;
    let perPage;
    let field;
    let order;
    // let filter;
    let query: any = {};
    if (params.pagination) {
      page = params.pagination.page;
      perPage =  params.pagination.perPage;
      query.page = page;
      query.size = perPage;

      // query.range = JSON.stringify([
      //   page,
      //   perPage,
      // ]);

    }
    if (params.sort) {
      field = params.sort.field;
      order = params.sort.order;
      if (field && order) {
        query.sort = (field + ',' + order);
      }
      else {
        query.sort = 'id,asc';
      }
    }
    if (!params.filter) {
      params.filter = {};
    }
    // if (Array.isArray(params.ids)) {
    //   params.filter.id = params.ids;
    // }

    if (params.filter) {
      query.filter = params.filter;
      console.log(params.filter);
    }
    console.log(query, stringify(query));
    return stringify(query);
  }

  // getTransactions(): Observable<Transaction> {
  //   return this.http.get<Transaction>(this.transactionUrl + 'list' + '?sort=id');
  // }
  //
  getTransaction(id: string): Observable<Object> {
    return this.http.get<Object>(this.apiPrefix + id);
  }

  updateTransaction(id: string, transaction: Transaction): Observable<Transaction> {
    console.log("Update service call");
    console.log(id, transaction);
    return this.http.put<Transaction>(this.apiPrefix + id, transaction);
  }
}
