import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Circle } from './circle.model';
import { CirclePopupService } from './circle-popup.service';
import { CircleService } from './circle.service';

@Component({
    selector: 'jhi-circle-delete-dialog',
    templateUrl: './circle-delete-dialog.component.html'
})
export class CircleDeleteDialogComponent {

    circle: Circle;

    constructor(
        private circleService: CircleService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.circleService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'circleListModification',
                content: 'Deleted an circle'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-circle-delete-popup',
    template: ''
})
export class CircleDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private circlePopupService: CirclePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.circlePopupService
                .open(CircleDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
