import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentManageAboutComponent } from './content-manage-about.component';

describe('ContentManageAboutComponent', () => {
  let component: ContentManageAboutComponent;
  let fixture: ComponentFixture<ContentManageAboutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ContentManageAboutComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ContentManageAboutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
