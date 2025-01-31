import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class GetAllUserService {
  baseUrl: string = "http://localhost:8080/admin/user/get-all";
  mainbaseUrl:string="http://localhost:8080/admin/user";

  constructor(
    private httpClint: HttpClient,
    private authService: AuthService
  ) { }

  // getAllUsers(): Observable<any> {
  //   if (this.authService.isLoggedIn()) {
  //     if (this.authService.isAdmin()) {
  //       return this.httpClint.get(this.baseUrl)
  //         .pipe(
  //           catchError(this.handelError)
  //         );
  //     } else {
  //       return throwError(() => new Error("User is not admin"));
  //     }

  //   } else {
  //     return throwError(() => new Error("Acount is not loggin"));
  //   }

  // }


  getAllUsers(page: number, size: number): Observable<any> {
    if (this.authService.isLoggedIn()) {
      if (this.authService.isAdmin()) {
        return this.httpClint.get(`${this.baseUrl}?page=${page}&size=${size}`)
          .pipe(
            catchError(this.handelError)
          );
      } else {
        return throwError(() => new Error("User is not admin"));
      }

    } else {
      return throwError(() => new Error("Acount is not loggin"));
    }

  }


  private handelError(error: any) {
    console.log("an error in get all user in service", error);
    return throwError(() => new Error(error.message || "Servcer Error"));
  }

  deleteUser(id: number): Observable<any> {
    if (this.authService.isLoggedIn()) {
      if (this.authService.isAdmin()) {
        return this.httpClint.delete(this.mainbaseUrl + '/'+id)
          .pipe(
            catchError(this.handelError)
          );
      } else {
        return throwError(() => new Error("User is not admin"));
      }

    } else {
      return throwError(() => new Error("Acount is not loggin"));
    }
  }
}
