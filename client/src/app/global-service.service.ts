import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { FormControl } from '@angular/forms/forms';
import { Observer, Subject } from 'rxjs';
import 'rxjs/Rx';
import { Product } from './product-page/product-page.component';


@Injectable()
export class GlobalService  {
  purchasedProductList: Product[] = [];
  totalPurchasedProductCount: number = 0;
  totalPurchasedProductAmount: number = 0.0;

  purchasedProductListChange: Subject<Product[]> = new Subject<Product[]>();
  totalPurchasedProductCountChange: Subject<number> = new Subject<number>();
  totalPurchasedProductAmountChange: Subject<number> = new Subject<number>();

  constructor(private http: Http) {

    this.getPurchasedProductList();
  }

  addProductToCart(webTransactionDao: Product) {
    this.http.post('http://localhost:8080/addCartItem', webTransactionDao)
      .subscribe(data => {
        if(data){
          this.purchasedProductList.push(webTransactionDao);
          this.purchasedProductList = this.purchasedProductList.slice();
          let quantity = 0;
          let totalAmount = 0;

          this.purchasedProductList.forEach((count)=>{
            quantity = +quantity +count.saleQuantity;
            totalAmount = +totalAmount +(count.saleQuantity * count.retail);
          });
          this.totalPurchasedProductCount = quantity;
          this.totalPurchasedProductAmount = totalAmount;

          this.purchasedProductListChange.next(this.purchasedProductList);
          this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
          this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);
          
          console.log('purchased product after add and slice',this.purchasedProductList )
        }
      },
      error => {
        console.log(JSON.stringify(error.json()));
      });
  }

  getPurchasedProductList(): Product[] {

    console.log('first',this.purchasedProductList )

    if(this.purchasedProductList.length <= 0){

      this.getPurchasedProductListFromBackEnd()
      .subscribe((purchasedProduct)=>{
        this.purchasedProductList = purchasedProduct;
        this.purchasedProductList = this.purchasedProductList.slice();
        console.log('purchased product list after service Call',this.purchasedProductList );
        this.purchasedProductListChange.next(this.purchasedProductList);
        let quantity = 0;
        let totalAmount = 0;
        this.purchasedProductList.forEach((count)=>{
          quantity = +quantity +count.saleQuantity;
          totalAmount = +totalAmount +(count.saleQuantity * count.retail);
        });
        this.totalPurchasedProductCount = quantity;
        this.totalPurchasedProductAmount = totalAmount;

        this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
        this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);
        
      }); 
    }
    else {
      // this is very important.
      this.purchasedProductListChange.next(this.purchasedProductList);
      let quantity = 0;
      let totalAmount = 0;
      this.purchasedProductList.forEach((count)=>{
        quantity = +quantity +count.saleQuantity;
        totalAmount = +totalAmount +(count.saleQuantity * count.retail);
      });
      this.totalPurchasedProductCount = quantity;
      this.totalPurchasedProductAmount = totalAmount;

      this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
      this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);
      
      return this.purchasedProductList;
    }

  }

  getPurchasedProductListFromBackEnd(): Observable<Product[]> {

    return this.http.get('http://localhost:8080/getCartItem?phoneNo=7707030801')
    .map(this.extractData)
    .catch(this.handleError);
  }

  deleteProductFromCart(webTransactionDao: Product){
    this.http.post('http://localhost:8080/deleteCartItem', webTransactionDao)
    .subscribe((data)=>{
      if(data.status == 200){
        let index = this.purchasedProductList.indexOf(webTransactionDao, 0);
        if (index > -1) {
          this.purchasedProductList.splice(index, 1);
          this.purchasedProductList = this.purchasedProductList.slice();
        }
        //this.purchasedProductList = this.purchasedProductList.slice();
        let quantity = 0;
        let totalAmount = 0;

        this.purchasedProductList.forEach((count)=>{
          quantity = +quantity +count.saleQuantity;
          totalAmount = +totalAmount +(count.saleQuantity * count.retail);
        });
        this.totalPurchasedProductCount = quantity;
        this.totalPurchasedProductAmount = totalAmount;

        this.purchasedProductListChange.next(this.purchasedProductList);
        this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
        this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);
        console.log("Delete done");
      }
    })
  }

  private extractData(res: Response) : Product[] {
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
