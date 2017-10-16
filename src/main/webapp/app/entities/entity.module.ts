import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { VerbboxBoxModule } from './box/box.module';
import { VerbboxVerbModule } from './verb/verb.module';
import { VerbboxPlayModule } from './play/play.module';
import { VerbboxVerbHistoryModule } from './verb-history/verb-history.module';
import { VerbboxCircleModule } from './circle/circle.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        VerbboxBoxModule,
        VerbboxVerbModule,
        VerbboxPlayModule,
        VerbboxVerbHistoryModule,
        VerbboxCircleModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class VerbboxEntityModule {}
