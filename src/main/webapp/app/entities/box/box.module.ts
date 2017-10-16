import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { VerbboxSharedModule } from '../../shared';
import { VerbboxAdminModule } from '../../admin/admin.module';
import {
    BoxService,
    BoxPopupService,
    BoxComponent,
    BoxDetailComponent,
    BoxDialogComponent,
    BoxPopupComponent,
    BoxDeletePopupComponent,
    BoxDeleteDialogComponent,
    boxRoute,
    boxPopupRoute,
} from './';

const ENTITY_STATES = [
    ...boxRoute,
    ...boxPopupRoute,
];

@NgModule({
    imports: [
        VerbboxSharedModule,
        VerbboxAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BoxComponent,
        BoxDetailComponent,
        BoxDialogComponent,
        BoxDeleteDialogComponent,
        BoxPopupComponent,
        BoxDeletePopupComponent,
    ],
    entryComponents: [
        BoxComponent,
        BoxDialogComponent,
        BoxPopupComponent,
        BoxDeleteDialogComponent,
        BoxDeletePopupComponent,
    ],
    providers: [
        BoxService,
        BoxPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class VerbboxBoxModule {}
