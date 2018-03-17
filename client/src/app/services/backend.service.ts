import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { FormControl } from '@angular/forms/forms';
import { Observer } from 'rxjs';
import 'rxjs/Rx';
import { Product } from '../product-page/product-page.component';

@Injectable()
export class BackendService {

  productList: Product [] = [];
  cartCount: number = 0;


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
    return this.http.get('http://localhost:8080/getEcommerceProductsByModel?modelId='+modelId)
    .map(this.extractData)
    .catch(this.handleError);

  }

  addProductToCart(webTransactionDao: Product) {
    console.log("Transaction Amount",webTransactionDao);
    this.http.post('http://localhost:8080/addCartItem', webTransactionDao)
      .subscribe(data => {
        alert('ok');
        console.log(data);

        if(data){
          this.productList.push(webTransactionDao);
        }
      },
      error => {
        console.log(JSON.stringify(error.json()));
      });

      this.productList = this.productList.slice();

      this.productList.forEach((list)=>{
        this.cartCount = +this.cartCount+list.saleQuantity;
      })
      
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

