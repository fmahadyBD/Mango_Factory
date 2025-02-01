import { Component, OnInit } from '@angular/core';
import { GetAllUserService } from '../../service/get-all-user.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-get-all-users',
  templateUrl: './get-all-users.component.html',
  styleUrl: './get-all-users.component.css'
})
export class GetAllUsersComponent implements OnInit {

  users: any[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalItems: number = 0;

  constructor(
    private getallUserService: GetAllUserService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    if (this.authService.isAdmin()) {
      this.loadAllUsers()
    }
  }


  loadAllUsers() {
    this.getallUserService.getAllUsers(this.currentPage - 1, this.itemsPerPage).subscribe({
      next: res => {
        this.users = res.content;
        this.totalItems = res.totalElements;
      },
      error: err => {
        console.log(err);
      }
    })
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.loadAllUsers();
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.itemsPerPage);
  }

  deleteUser(id: number) {
    this.getallUserService.deleteUser(id).subscribe({
      next: res => {
        console.log("Successfully Delete ", res);

        // remove the deleted user from the list
        this.users = this.users.filter(user => user.id != id);
        this.totalItems -= 1
      },
      error: ee => {
        console.log(ee);
      }
    })
  }
}
