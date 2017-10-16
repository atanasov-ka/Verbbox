import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { VerbboxSharedModule } from '../../shared';
import {
    VerbHistoryService,
    VerbHistoryPopupService,
    VerbHistoryComponent,
    VerbHistoryDetailComponent,
    VerbHistoryDialogComponent,
    VerbHistoryPopupComponent,
    VerbHistoryDeletePopupComponent,
    VerbHistoryDeleteDialogComponent,
    verbHistoryRoute,
    verbHistoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...verbHistoryRoute,
    ...verbHistoryPopupRoute,
];

@NgModule({
    imports: [
        VerbboxSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        VerbHistoryComponent,
        VerbHistoryDetailComponent,
        VerbHistoryDialogComponent,
        VerbHistoryDeleteDialogComponent,
        VerbHistoryPopupComponent,
        VerbHistoryDeletePopupComponent,
    ],
    entryComponents: [
        VerbHistoryComponent,
        VerbHistoryDialogComponent,
        VerbHistoryPopupComponent,
        VerbHistoryDeleteDialogComponent,
        VerbHistoryDeletePopupComponent,
    ],
    providers: [
        VerbHistoryService,
        VerbHistoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class VerbboxVerbHistoryModule {}
