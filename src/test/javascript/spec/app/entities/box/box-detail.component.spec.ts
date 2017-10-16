/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { VerbboxTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BoxDetailComponent } from '../../../../../../main/webapp/app/entities/box/box-detail.component';
import { BoxService } from '../../../../../../main/webapp/app/entities/box/box.service';
import { Box } from '../../../../../../main/webapp/app/entities/box/box.model';

describe('Component Tests', () => {

    describe('Box Management Detail Component', () => {
        let comp: BoxDetailComponent;
        let fixture: ComponentFixture<BoxDetailComponent>;
        let service: BoxService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [VerbboxTestModule],
                declarations: [BoxDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BoxService,
                    JhiEventManager
                ]
            }).overrideTemplate(BoxDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BoxDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BoxService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Box(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.box).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
