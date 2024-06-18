import {inject, Injectable, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {BaseMessage} from "@domain/message/base.message";
import {CompatClient, Stomp, StompSubscription} from "@stomp/stompjs";
import {Subject} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root',
})
export class GameWebsocket implements OnDestroy {
  private connection: CompatClient;
  private subscriptions: StompSubscription;
  private route = inject(ActivatedRoute);

  private messageSubject = new Subject<BaseMessage>();
  message$ = this.messageSubject.asObservable();

  constructor(){
    this.connection = Stomp.client(environment.wsUrl);
  }

  connect(gameId: string){
    this.connection.connect({}, () => {
      console.log('Connected to game WS')
      this.subscriptions = this.connection.subscribe(`/topic/game/${gameId}`, ( message ) => {
        this.messageSubject.next(JSON.parse(message.body) as BaseMessage);
        console.log('Message from game' ,message)
      });
    });
  }

  ngOnDestroy(): void {
    if (this.subscriptions) {
      this.subscriptions.unsubscribe();
    }
    this.connection.disconnect();
  }
}
