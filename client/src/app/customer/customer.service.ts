import {Injectable } from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { FormControl } from '@angular/forms/forms';
import { Customer } from './customer.component';



@Injectable()
export class CustomerService {
private url: string; 

 

constructor(private http: Http) { 
 // this.url = environment.reportUrl;
  //this.getCustomerDetails();
}

// getCustomerDetails(): Customer[]
// {
//   if(this.customerList && this.customerList.length <= 0){
//     this.getCustomerDetailsFromBackEnd()
//     .subscribe((cust)=>{
//       this.customerList = cust;
//       this.customerListChange.next(this.customerList);
//       return this.customerList;
//     })
//   }
//   else {
//     console.log('Customer List alredy exists', this.customerList)
//     this.customerListChange.next(this.customerList);
//     return this.customerList;
//   }
// }

//     getCustomerDetailsFromBackEnd(): Observable<Customer[]> {
//       return this.http.get(this.url+'/getCustomer')
//       .map(this.extractData)
//       .catch(this.handleError);
//     }

//     getCustomerDetailsByPhoneNo(phoneNo: any): Observable<Customer> {
//       return this.http.get(this.url+'/getCustomerByPhoneNo?phoneNo='+phoneNo)
//       .map(this.extractData)
//       .catch(this.handleError);
//     }

//     getCustomerStoreCreditHistory(phoneNo: any): Observable<StoreCreditDto[]> {
//       return this.http.get(this.url+'/getCustomerStoreCreditHistory?phoneNo='+phoneNo)
//       .map(this.extractData)
//       .catch(this.handleError);
//     }

//     getCustomerTransactionDetails()
//     {
      
//     }

    addOrUpdateCustomer(customer: Customer)
    {
      this.http.post(this.url+'/addCustomer', customer)
      .subscribe(data => {
        if(data.status == 200 || data.status == 201){

        }
      },
        error => {
          console.log(JSON.stringify(error.json()));
    });
    }

    private extractData(res: Response): Customer[] {
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