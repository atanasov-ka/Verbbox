import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Verb } from './verb.model';
import { VerbPopupService } from './verb-popup.service';
import { VerbService } from './verb.service';

@Component({
    selector: 'jhi-verb-delete-dialog',
    templateUrl: './verb-delete-dialog.component.html'
})
export class VerbDeleteDialogComponent {

    verb: Verb;

    constructor(
        private verbService: VerbService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.verbService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'verbListModification',
                content: 'Deleted an verb'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-verb-delete-popup',
    template: ''
})
export class VerbDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private verbPopupService: VerbPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.verbPopupService
                .open(VerbDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
