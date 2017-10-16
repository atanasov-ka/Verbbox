/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { VerbboxTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PlayDetailComponent } from '../../../../../../main/webapp/app/entities/play/play-detail.component';
import { PlayService } from '../../../../../../main/webapp/app/entities/play/play.service';
import { Play } from '../../../../../../main/webapp/app/entities/play/play.model';

describe('Component Tests', () => {

    describe('Play Management Detail Component', () => {
        let comp: PlayDetailComponent;
        let fixture: ComponentFixture<PlayDetailComponent>;
        let service: PlayService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [VerbboxTestModule],
                declarations: [PlayDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PlayService,
                    JhiEventManager
                ]
            }).overrideTemplate(PlayDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PlayDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlayService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Play(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.play).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
