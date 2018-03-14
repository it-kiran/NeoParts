import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { FormControl } from '@angular/forms/forms';
import { Observer } from 'rxjs';
import { Product } from '../components/sell/sell.component';
import 'rxjs/Rx';

@Injectable()
export class BackendService {

  private apiURL = 'http://localhost:8080/getWebMenu';
  private searchURL = 'http://localhost:8080/getAllProductForSearch';

  data:any = {};


  appleInfo:any=[];

  constructor(private http:Http) { }

  getData(){
    return this.http.get(this.apiURL, { headers: new Headers({ 'Content-Type': 'application/json' }) })
    .map((res:Response)=>res.json());
  }

  getFilterSearch(value){
    return this.http.get(this.searchURL, { headers: new Headers({ 'Content-Type': 'application/json' }) })
    .map((res:Response)=>res.json());
  }

  getProductByModelId(modelId: number): Observable<Product[]> {
    return this.http.get('http://localhost:8080/getEcommerceProductsByModel?modelId=111')
    .map(this.extractData)
    .catch(this.handleError);

  }

  private extractData(res: Response): Product[] {
    let body = res.json();
    // console.log(body);
    return body || {};
  }
  
  private handleError(error: Response | any) {
    // In a real world app, you might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }

}

