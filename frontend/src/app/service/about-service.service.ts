import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AboutServiceService {

  private baseUrl='';

  aboutService(

    about:{
      header:string;
      description:string;
      address:string;
      image:string;
    },image:File

  ):Observable<any>{

    const formData = new FormData();
    formData.append('about',new Blob( 
      
      [JSON.stringify(about)], 
      {type:'application/json'}
  
    ))

    formData.append('image',image);
    return this.http.post<any>(this.baseUrl)
  }

  constructor(
    private http:HttpClient,
    private router:Router
  ) { }
}
