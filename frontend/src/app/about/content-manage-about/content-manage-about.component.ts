import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-content-manage-about',
  templateUrl: './content-manage-about.component.html',
  styleUrl: './content-manage-about.component.css'
})
export class ContentManageAboutComponent {
  aboutForm:FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  image: File | null = null;

  constructor(
    private fb:FormBuilder,
    
    private router:Router
  ){

    this.aboutForm = this.fb.group({
      header:[''],
      address:[''],
      description:['']

    })
  }

  onFileSelected(event:Event):void{
    const input = event.target as HTMLInputElement;
    if(input?.files && input.files[0]){
      this.image =input.files[0];
    }
  }

  onSubmit(){
    const{header,address,desription, image}=this.aboutForm.value;
    this.aboutService.save(
      {
        header,address,desription, image:''
      },
      this.image
    ).subscribe({
      next:()=>{
        this.router.navigate(['']);
      },error=>{
        console.log(error);
        
        
      }
    })
  }


}
