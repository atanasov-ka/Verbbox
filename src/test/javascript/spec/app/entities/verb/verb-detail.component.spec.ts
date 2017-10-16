/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { VerbboxTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VerbDetailComponent } from '../../../../../../main/webapp/app/entities/verb/verb-detail.component';
import { VerbService } from '../../../../../../main/webapp/app/entities/verb/verb.service';
import { Verb } from '../../../../../../main/webapp/app/entities/verb/verb.model';

describe('Component Tests', () => {

    describe('Verb Management Detail Component', () => {
        let comp: VerbDetailComponent;
        let fixture: ComponentFixture<VerbDetailComponent>;
        let service: VerbService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [VerbboxTestModule],
                declarations: [VerbDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VerbService,
                    JhiEventManager
                ]
            }).overrideTemplate(VerbDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VerbDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VerbService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Verb(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.verb).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
