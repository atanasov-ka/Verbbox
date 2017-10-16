/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { VerbboxTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CircleDetailComponent } from '../../../../../../main/webapp/app/entities/circle/circle-detail.component';
import { CircleService } from '../../../../../../main/webapp/app/entities/circle/circle.service';
import { Circle } from '../../../../../../main/webapp/app/entities/circle/circle.model';

describe('Component Tests', () => {

    describe('Circle Management Detail Component', () => {
        let comp: CircleDetailComponent;
        let fixture: ComponentFixture<CircleDetailComponent>;
        let service: CircleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [VerbboxTestModule],
                declarations: [CircleDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CircleService,
                    JhiEventManager
                ]
            }).overrideTemplate(CircleDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CircleDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CircleService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Circle(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.circle).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
