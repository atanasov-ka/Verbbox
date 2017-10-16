/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { VerbboxTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VerbHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/verb-history/verb-history-detail.component';
import { VerbHistoryService } from '../../../../../../main/webapp/app/entities/verb-history/verb-history.service';
import { VerbHistory } from '../../../../../../main/webapp/app/entities/verb-history/verb-history.model';

describe('Component Tests', () => {

    describe('VerbHistory Management Detail Component', () => {
        let comp: VerbHistoryDetailComponent;
        let fixture: ComponentFixture<VerbHistoryDetailComponent>;
        let service: VerbHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [VerbboxTestModule],
                declarations: [VerbHistoryDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VerbHistoryService,
                    JhiEventManager
                ]
            }).overrideTemplate(VerbHistoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VerbHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VerbHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new VerbHistory(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.verbHistory).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
