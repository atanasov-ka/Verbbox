import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { VerbboxBoxModule } from './box/box.module';
import { VerbboxVerbModule } from './verb/verb.module';
import { VerbboxPlayModule } from './play/play.module';
import { VerbboxVerbHistoryModule } from './verb-history/verb-history.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        VerbboxBoxModule,
        VerbboxVerbModule,
        VerbboxPlayModule,
        VerbboxVerbHistoryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class VerbboxEntityModule {}
