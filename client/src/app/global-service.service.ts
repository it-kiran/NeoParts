import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { FormControl } from '@angular/forms/forms';
import { Observer, Subject } from 'rxjs';
import 'rxjs/Rx';
import { Product } from './product-page/product-page.component';
import { Customer } from './customer/customer.component';
import { environment } from '../environments/environment';
import { CustomerService } from './customer/customer.service';
import { ServicesService } from './shared/services.service';
import { element } from 'protractor';
import { Router, ActivatedRoute } from '@angular/router';



@Injectable()
export class GlobalService {
  private url: string;
  purchasedProductList: Product[] = [];
  totalPurchasedProductCount: number = 0;
  totalPurchasedProductAmount: number = 0.0;

  loginedCustomer: Customer;

  purchasedProductListChange: Subject<Product[]> = new Subject<Product[]>();
  totalPurchasedProductCountChange: Subject<number> = new Subject<number>();
  totalPurchasedProductAmountChange: Subject<number> = new Subject<number>();

  constructor(private http: Http, private customerService: CustomerService, private persistService: ServicesService, private route: ActivatedRoute, private router: Router) {
    this.url = environment.productUrl;
    //this.getPurchasedProductList();
  }

  addProductToCart(webTransactionDao: Product) {
    console.log('sale quanity frist check', webTransactionDao.saleQuantity);
    console.log('before push to product list', this.purchasedProductList);

    let headers = new Headers({
      'Authorization': 'Bearer ' + this.customerService.getToken()
    });

    this.http.post(this.url + '/addCartItem', webTransactionDao, { headers: headers })
      .subscribe(data => {
        if (data) {
          this.purchasedProductList = this.purchasedProductList.slice();
          console.log('just after data check and slice.', this.purchasedProductList);

          this.purchasedProductList.push(webTransactionDao);
          console.log('after push to product list', this.purchasedProductList);

          this.purchasedProductList = this.purchasedProductList.slice();
          console.log('sale quanity third check', this.purchasedProductList);

          let quantity = 0;
          let totalAmount = 0;

          this.purchasedProductList.forEach((count) => {
            quantity = +quantity + count.saleQuantity;
            totalAmount = +totalAmount + (count.saleQuantity * count.retail);
          });
          // To set decimal values to 2 digits.
          totalAmount = Math.round(totalAmount * 1e2) / 1e2;

          console.log('sale quanity forth check', quantity);

          this.totalPurchasedProductCount = quantity;
          this.totalPurchasedProductAmount = totalAmount;

          this.purchasedProductListChange.next(this.purchasedProductList);
          this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
          this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);

          console.log('purchased product after add and slice', this.purchasedProductList)
        }
      },
        error => {
          console.log(JSON.stringify(error.json()));
        });
  }
  updateProductFromCart(webTransactionDao: Product) {

    let headers = new Headers({
      'Authorization': 'Bearer ' + this.customerService.getToken()
    });
    this.http.post(this.url + '/addCartItem', webTransactionDao, { headers: headers })
      .subscribe(data => {
        if (data) {
          console.log('sale quanity second check', data.json().saleQuantity);

          let index = this.purchasedProductList.findIndex((el) => el.productNo == webTransactionDao.productNo);
          if (index > -1) {
            this.purchasedProductList[index].saleQuantity = webTransactionDao.saleQuantity;
            this.purchasedProductList = this.purchasedProductList.slice();
          }
          let quantity = 0;
          let totalAmount = 0;

          this.purchasedProductList.forEach((count) => {
            quantity = +quantity + count.saleQuantity;
            totalAmount = +totalAmount + (count.saleQuantity * count.retail);
          });

          // To set decimal values to 2 digits.
          totalAmount = Math.round(totalAmount * 1e2) / 1e2;
          console.log('sale quanity forth check', quantity);

          this.totalPurchasedProductCount = quantity;
          this.totalPurchasedProductAmount = totalAmount;

          this.purchasedProductListChange.next(this.purchasedProductList);
          this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
          this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);

          console.log('purchased product after add and slice', this.purchasedProductList)
        }
      },
        error => {
          console.log(JSON.stringify(error.json()));
        });
  }

  getPurchasedProductList(): Product[] {

    console.log('first', this.purchasedProductList)

    if (this.purchasedProductList.length <= 0) {

      this.getPurchasedProductListFromBackEnd()
        .subscribe((purchasedProduct) => {
          this.purchasedProductList = purchasedProduct;
          this.purchasedProductList = this.purchasedProductList.slice();
          console.log('purchased product list after service Call', this.purchasedProductList);
          this.purchasedProductListChange.next(this.purchasedProductList);
          let quantity = 0;
          let totalAmount = 0;


          this.purchasedProductList.forEach((count) => {
            quantity = +quantity + count.saleQuantity;
            totalAmount = +totalAmount + (count.saleQuantity * count.retail);
          });

          // To set decimal values to 2 digits.
          totalAmount = Math.round(totalAmount * 1e2) / 1e2;

          this.totalPurchasedProductCount = quantity;
          this.totalPurchasedProductAmount = totalAmount;

          this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
          this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);

        });

      return this.purchasedProductList;
    }
    else {
      // this is very important.
      this.purchasedProductListChange.next(this.purchasedProductList);
      let quantity = 0;
      let totalAmount = 0;
      this.purchasedProductList.forEach((count) => {
        quantity = +quantity + count.saleQuantity;
        totalAmount = +totalAmount + (count.saleQuantity * count.retail);
      });
      // To set decimal values to 2 digits.
      totalAmount = Math.round(totalAmount * 1e2) / 1e2;

      this.totalPurchasedProductCount = quantity;
      this.totalPurchasedProductAmount = totalAmount;

      this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
      this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);

      return this.purchasedProductList;
    }

  }

  getPurchasedProductListFromBackEnd(): Observable<Product[]> {
    let headers = new Headers({
      'Authorization': 'Bearer ' + this.customerService.getToken()
    });

    let token = this.customerService.getToken();
    let selectedCustomer: Customer = this.persistService.getCustomerDetailsForSale();
    if (token && selectedCustomer) {
      return this.http.get(this.url + '/getCartItem?phoneNo=' + selectedCustomer.phoneNo, { headers: headers })
        .map(this.extractData)
        .catch(this.handleError);
    }
    else {
      return null;
    }

  }

  deleteProductFromCart(webTransactionDao: Product) {

    let headers = new Headers({
      'Authorization': 'Bearer ' + this.customerService.getToken()
    });
    console.log('product Coming for delete', webTransactionDao);
    this.http.post(this.url + '/deleteCartItem', webTransactionDao, { headers: headers })
      .subscribe((data) => {
        if (data.status == 200) {
          let index = this.purchasedProductList.indexOf(webTransactionDao, 0);
          if (index > -1) {
            this.purchasedProductList.splice(index, 1);
            this.purchasedProductList = this.purchasedProductList.slice();
          }
          //this.purchasedProductList = this.purchasedProductList.slice();
          let quantity = 0;
          let totalAmount = 0;

          this.purchasedProductList.forEach((count) => {
            quantity = +quantity + count.saleQuantity;
            totalAmount = +totalAmount + (count.saleQuantity * count.retail);
          });
          // To set decimal values to 2 digits.
          totalAmount = Math.round(totalAmount * 1e2) / 1e2;
          this.totalPurchasedProductCount = quantity;
          this.totalPurchasedProductAmount = totalAmount;

          this.purchasedProductListChange.next(this.purchasedProductList);
          this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
          this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);
          console.log("Delete done");
        }
      })
  }

  clearCart(username: string) {
    let headers = new Headers({
      'Authorization': 'Bearer ' + this.customerService.getToken()
    });
    this.http.delete(this.url + '/clearCart?username=' + username, { headers: headers })
      .subscribe((data) => {
        if (data.status == 200) {
          this.purchasedProductList = [];
          //this.purchasedProductList = this.purchasedProductList.slice();
          let quantity = 0;
          let totalAmount = 0;

          this.purchasedProductList.forEach((count) => {
            quantity = +quantity + count.saleQuantity;
            totalAmount = +totalAmount + (count.saleQuantity * count.retail);

          });
          // To set decimal values to 2 digits.
          totalAmount = Math.round(totalAmount * 1e2) / 1e2;
          this.totalPurchasedProductCount = quantity;
          this.totalPurchasedProductAmount = totalAmount;

          this.purchasedProductListChange.next(this.purchasedProductList);
          this.totalPurchasedProductCountChange.next(this.totalPurchasedProductCount);
          this.totalPurchasedProductAmountChange.next(this.totalPurchasedProductAmount);

          this.router.navigate(['']);

          console.log("clear cart is done!!");
        }
      })

  }

  // I am using these two methods at all over the application.
  getLoginedCustomer(): Customer {
    return this.loginedCustomer;
  }
  setLoginedCustomer(loginCustomer: Customer) {
    this.loginedCustomer = loginCustomer
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
