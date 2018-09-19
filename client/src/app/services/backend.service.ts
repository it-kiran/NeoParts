import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { FormControl } from '@angular/forms/forms';
import { Observer } from 'rxjs';
import 'rxjs/Rx';
import { Product } from '../product-page/product-page.component';
import { environment } from '../../environments/environment';
import { CustomerService } from '../customer/customer.service';

@Injectable()
export class BackendService {
  private url: string;

  productList: Product[] = [];
  cartCount: number = 0;

  //private apiURL = this.url+'/getWebMenu';
  //private searchURL = this.url+'/getAllProductForSearch';

  data: any = {};


  appleInfo: any = [];

  private headers = new Headers({
    'Authorization': 'Bearer ' + this.customerService.getToken()
  });

  constructor(private http: Http, private customerService: CustomerService) {
    this.url = environment.productUrl;
  }

  getAllProducts() {

    let headers = new Headers({
      'Authorization': 'Bearer ' + this.customerService.getToken()
    });

    return this.http.get(this.url + '/getAllProduct', { headers: headers })
      .map(this.extractData)
      .catch(this.handleError);
  }

  getData() {
    return this.http.get(this.url + '/getWebMenu', { headers: this.headers })
      .map((res: Response) => res.json());
  }


  getFilterSearch(value) {
    return this.http.get(this.url + '/getAllProductForSearch', { headers: this.headers })
      .map((res: Response) => res.json());
  }

  getProductByModelId(modelId: number): Observable<Product[]> {
    return this.http.get(this.url + '/getEcommerceProductsByModel?modelId=' + modelId, { headers: this.headers })
      .map(this.extractData)
      .catch(this.handleError);
  }
  getProductByCategoryId(categoryId: number): Observable<Product[]> {
    return this.http.get(this.url + '/getProductsByCategory?categoryId=' + categoryId, { headers: this.headers })
      .map(this.extractData)
      .catch(this.handleError);
  }


  addImage(productNo: string, image: any) {

    let headers = new Headers({
      'Authorization': 'Bearer ' + this.customerService.getToken()
    });

    console.log('Customer to be Added' + image);
    this.http.post(this.url + '/insertProductImage?productNo=' + productNo, image, { headers: headers })
      .subscribe(data => {
        console.log('Response From Add Customer call' + data);
      },
        error => {
          console.log(JSON.stringify(error.json()));
        });
  }

  getFeaturedProduct(): Observable<Product[]>{
    return this.http.get(this.url + '/getFeaturedProducts')
    .map(this.extractData)
    .catch(this.handleError);
  }
  
  getNewProducts(){
    return this.http.get(this.url + '/getNewProducts')
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

