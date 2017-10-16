import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Box } from './box.model';
import { BoxPopupService } from './box-popup.service';
import { BoxService } from './box.service';

@Component({
    selector: 'jhi-box-delete-dialog',
    templateUrl: './box-delete-dialog.component.html'
})
export class BoxDeleteDialogComponent {

    box: Box;

    constructor(
        private boxService: BoxService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.boxService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'boxListModification',
                content: 'Deleted an box'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-box-delete-popup',
    template: ''
})
export class BoxDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private boxPopupService: BoxPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.boxPopupService
                .open(BoxDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
