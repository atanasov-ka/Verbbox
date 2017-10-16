import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { VerbboxSharedModule } from '../../shared';
import { VerbboxAdminModule } from '../../admin/admin.module';
import {
    CircleService,
    CirclePopupService,
    CircleComponent,
    CircleDetailComponent,
    CircleDialogComponent,
    CirclePopupComponent,
    CircleDeletePopupComponent,
    CircleDeleteDialogComponent,
    circleRoute,
    circlePopupRoute,
} from './';

const ENTITY_STATES = [
    ...circleRoute,
    ...circlePopupRoute,
];

@NgModule({
    imports: [
        VerbboxSharedModule,
        VerbboxAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CircleComponent,
        CircleDetailComponent,
        CircleDialogComponent,
        CircleDeleteDialogComponent,
        CirclePopupComponent,
        CircleDeletePopupComponent,
    ],
    entryComponents: [
        CircleComponent,
        CircleDialogComponent,
        CirclePopupComponent,
        CircleDeleteDialogComponent,
        CircleDeletePopupComponent,
    ],
    providers: [
        CircleService,
        CirclePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class VerbboxCircleModule {}
