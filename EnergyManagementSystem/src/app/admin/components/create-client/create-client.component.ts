import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-create-client',
  templateUrl: './create-client.component.html',
  styleUrls: ['./create-client.component.scss']
})
export class CreateClientComponent {

  signupForm!: FormGroup;
  hidePassword= true;


constructor(private fb: FormBuilder,
  private snackBar: MatSnackBar,
  private router: Router,
  private authService: AuthService
){

}

ngOnInit(): void{
  this.signupForm = this.fb.group({
    name: [null, [Validators.required]],
    age: [null, [Validators.required]],
    address: [null, [Validators.required, Validators.email]],
    password: [null, [Validators.required]],
    confirmPassword: [null, [Validators.required]],
  })
}

togglePasswordVisibility(){
  this.hidePassword = !this.hidePassword;
}

onSubmit(): void{
  const password = this.signupForm.get('password')?.value;
  const confirmPassword = this.signupForm.get('confirmPassword')?.value;

  if(password !== confirmPassword){
    this.snackBar.open('Passwords do not match.','close',{duration: 5000, panelClass:'error-snackbar'});
    return;
  }

  this.authService.register(this.signupForm.value).subscribe(
    (response) =>{
      this.snackBar.open('Client Create succesful!','close',{ duration : 5000});
      this.router.navigateByUrl("/admin/clients");
    },
    (error) =>{
      this.snackBar.open('Sign Up failed. Please try again.','close',{duration: 5000, panelClass:'error-snackbar'});
    }
  )
}

goBack(): void {
  this.router.navigate(['admin/clients']);
}


}
