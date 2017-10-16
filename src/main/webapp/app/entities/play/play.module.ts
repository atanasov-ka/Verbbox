import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { VerbboxSharedModule } from '../../shared';
import { VerbboxAdminModule } from '../../admin/admin.module';
import {
    PlayService,
    PlayPopupService,
    PlayComponent,
    PlayDetailComponent,
    PlayDialogComponent,
    PlayPopupComponent,
    PlayDeletePopupComponent,
    PlayDeleteDialogComponent,
    playRoute,
    playPopupRoute,
} from './';

const ENTITY_STATES = [
    ...playRoute,
    ...playPopupRoute,
];

@NgModule({
    imports: [
        VerbboxSharedModule,
        VerbboxAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PlayComponent,
        PlayDetailComponent,
        PlayDialogComponent,
        PlayDeleteDialogComponent,
        PlayPopupComponent,
        PlayDeletePopupComponent,
    ],
    entryComponents: [
        PlayComponent,
        PlayDialogComponent,
        PlayPopupComponent,
        PlayDeleteDialogComponent,
        PlayDeletePopupComponent,
    ],
    providers: [
        PlayService,
        PlayPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class VerbboxPlayModule {}
