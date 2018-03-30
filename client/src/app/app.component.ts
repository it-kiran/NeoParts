import { Component, OnInit } from '@angular/core';
import { Http,Response } from '@angular/http';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import {BackendService} from './services/backend.service';
import { GlobalService } from './global-service.service';
import { Product } from './product-page/product-page.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  test: Product[] = []
  constructor(private globalService: GlobalService){}

  ngOnInit() {
    // console.log('data from app component',this.globalService.getPurchasedProductList());

    if(this.globalService.purchasedProductList.length > 0){
      this.test = this.globalService.purchasedProductList;
    }
    else {
      this.globalService.getPurchasedProductListFromBackEnd()
      .subscribe((test:Product[])=>{
        this.test = test;
        console.log('final test1', this.test);

      })
    }
  
    console.log('final test2', this.test);
  }


//   title = 'app';
//   searchText: string;

//   backendData : any=[];

//   appleData =[];
//   appleDataIphone :any=[];
//   appleDataIpad :any=[];

//   samsungData :any=[];
//   samsungNote :any=[];
//   samsungS :any=[];
//   samsungOther :any=[];

//   searchData:any=[];
//   sss=false;
//   otherData=[];
//   aaa=[0,1,2,3,4];

// noofshow = 5;
// values = '';




// inputOutsideClicked:boolean;
// characters = [
//   'Finn the human11',
//   'Finn the human1111',
//   'Finn the human1111111',
//   'Jake the dog11',
//   'Princess bubblegum12',
//   'Lumpy Space Princess123',
//   'Beemo1',
//   'Beemo2'
// ]


  // constructor(private http:Http,public backendService:BackendService,private router: Router){
  //    this.backendService.getData()
  //     .subscribe(data=>{
  //       console.log(data)
  //       for(var i =0;i<data.webBrandDtoList.length;i++){
  //         var str = data.webBrandDtoList[i].brandName;
  //         var mm = str.includes("APPLE");
  //         var nm = str.includes("SAMSUNG");

  //         if(mm){
  //           this.appleData.push(data.webBrandDtoList[i]);

  //           this.backendService.appleInfo.push(
  //             {brandId:this.appleData[0].brandId ,
  //             brandName:this.appleData[0].brandName
  //              }
  //           );
  //         }
  //         else if(nm){
  //           this.samsungData.push(data.webBrandDtoList[i]);}
  //         else{
  //           if( data.webBrandDtoList[i].modelDtoList.length){
  //             if(data.webBrandDtoList[i].modelDtoList.length > this.noofshow){
  //               var cntiteration = new Array( );
  //               for(var k = 0 ; k <= Math.floor((data.webBrandDtoList[i].modelDtoList.length - this.noofshow)/this.noofshow); k ++){
  //                 cntiteration[k] = new Array( );
  //               }
  //               for(var j = this.noofshow ; j < data.webBrandDtoList[i].modelDtoList.length ; j ++){
  //                 cntiteration[Math.floor((j-this.noofshow)/5)].push(j);
  //               }
  //               data.webBrandDtoList[i]['cntiteration'] = cntiteration;
  //             }
  //             this.otherData.push(data.webBrandDtoList[i]);
  //           }
  //         }
  //       }
  //       // console.log(this.otherData)
  //       for(var i = 0;i<this.appleData[0].modelDtoList.length;i++){
  //         var str = this.appleData[0].modelDtoList[i];
  //         var m = str.name.includes("iPhone");
  //         var n = str.name.includes("iPad");

  //         if(m){
  //           this.appleDataIphone.push(str)
  //           // console.log(this.appleDataIphone);
  //         }
  //         if(n){
  //           this.appleDataIpad.push(str)
  //         }
  //         else{}
  //       }

  //       for(var i = 0;i<this.samsungData[0].modelDtoList.length;i++){
  //         var str = this.samsungData[0].modelDtoList[i].name;
  //         var note = str.includes("Galaxy Note");
  //         var s = str.includes("Galaxy S");

  //         if(note){
  //           this.samsungNote.push(str)
  //         }
  //         else if(s){
  //           this.samsungS.push(str)
  //         }
  //         else{
  //           this.samsungOther.push(str)
  //         }
  //       }



  //     });



  // }


  // onKey(event: any) { // without type info
  //   this.values='';
  //   this.values = event.target.value ;
  //   this.backendService.getFilterSearch(this.values)
  //   .subscribe(data=>{
  //     for(var i = 0; i <data.length;i++){
  //       var str = data[i].description;
  //         var mm = str.includes(this.values);
  //         if(mm){
  //           this.searchData.push(
  //             data[i]
  //           )}
  //     }
  //     // console.log(this.searchData)
  //   })
  // }

  // onClickedOutside(e: Event) {
  //   if(e.isTrusted){
  //     this.inputOutsideClicked = true;
  //   }
  //   else{
  //     this.inputOutsideClicked = false;
  //   }
  //   // console.log('Clicked outside:', e);
  // }



}
